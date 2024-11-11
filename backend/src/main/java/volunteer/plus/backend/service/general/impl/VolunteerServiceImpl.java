package volunteer.plus.backend.service.general.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import volunteer.plus.backend.domain.dto.VolunteerDTO;
import volunteer.plus.backend.domain.dto.VolunteerFeedbackPayloadDTO;
import volunteer.plus.backend.domain.entity.Volunteer;
import volunteer.plus.backend.domain.entity.VolunteerFeedback;
import volunteer.plus.backend.exceptions.ApiException;
import volunteer.plus.backend.exceptions.ErrorCode;
import volunteer.plus.backend.repository.UserRepository;
import volunteer.plus.backend.repository.VolunteerFeedbackRepository;
import volunteer.plus.backend.repository.VolunteerRepository;
import volunteer.plus.backend.service.email.EmailNotificationBuilderService;
import volunteer.plus.backend.service.general.VolunteerService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;


@Slf4j
@Service
@RequiredArgsConstructor
public class VolunteerServiceImpl implements VolunteerService {
    private final VolunteerRepository volunteerRepository;
    private final VolunteerFeedbackRepository volunteerFeedbackRepository;
    private final UserRepository userRepository;
    private final EmailNotificationBuilderService emailNotificationBuilderService;

    @Override
    public Page<VolunteerDTO> getVolunteers(final Pageable pageable,
                                            final Set<Long> volunteerIds) {
        final Page<Volunteer> volunteers;
        if (volunteerIds == null) {
            log.info("Retrieve all volunteers by page: {}, and size: {}", pageable.getPageNumber(), pageable.getPageSize());
            volunteers = volunteerRepository.findAll(pageable);
        } else {
            log.info("Retrieve all volunteers by ids size: {}", volunteerIds);
            volunteers = volunteerRepository.findAllByIdIn(volunteerIds, pageable);
        }
        return volunteers.map(this::mapVolunteerToDTO);
    }

    @Override
    @Transactional
    public VolunteerDTO createVolunteer(final String userEmail) {
        final var user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));

        if (user.getVolunteer() != null) {
            throw new ApiException(ErrorCode.VOLUNTEER_IS_ALREADY_ASSIGNED_TO_USER);
        }

        final var volunteer = Volunteer.builder()
                .user(user)
                .levies(new ArrayList<>())
                .volunteerFeedbacks(new ArrayList<>())
                .build();

        return mapVolunteerToDTO(volunteerRepository.saveAndFlush(volunteer));
    }

    @Override
    @Transactional
    public VolunteerDTO createOrUpdateFeedback(final String userEmail,
                                               final VolunteerFeedbackPayloadDTO volunteerFeedbackPayloadDTO) {
        final var user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));

        final var volunteer = volunteerRepository.findById(volunteerFeedbackPayloadDTO.getVolunteerId())
                .orElseThrow(() -> new ApiException(ErrorCode.VOLUNTEER_NOT_FOUND));

        if (volunteerFeedbackPayloadDTO.getFeedbackId() != null) {
            final var feedback = volunteer.getVolunteerFeedbacks()
                    .stream()
                    .filter(vf -> Objects.equals(vf.getId(), volunteerFeedbackPayloadDTO.getFeedbackId()))
                    .findFirst();

            if (feedback.isEmpty()) {
                throw new ApiException(ErrorCode.VOLUNTEER_FEEDBACK_NOT_FOUND);
            }

            feedback.get().setText(volunteerFeedbackPayloadDTO.getText());
            feedback.get().setReputationScore(volunteerFeedbackPayloadDTO.getScore());

            return mapVolunteerToDTO(volunteerRepository.save(volunteer));
        } else {
            final var feedback = VolunteerFeedback.builder()
                    .reputationScore(volunteerFeedbackPayloadDTO.getScore())
                    .text(volunteerFeedbackPayloadDTO.getText())
                    .build();

            volunteerFeedbackRepository.saveAndFlush(feedback);

            volunteer.addFeedback(feedback);

            final var volunteerSaved = volunteerRepository.saveAndFlush(volunteer);

            user.addFeedback(feedback);

            userRepository.saveAndFlush(user);

            emailNotificationBuilderService.createVolunteerFeedbackEmail(feedback);

            return mapVolunteerToDTO(volunteerSaved);
        }
    }

    @Override
    @Transactional
    public void removeFeedback(final Long feedbackId) {
        final var feedback = volunteerFeedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new ApiException(ErrorCode.VOLUNTEER_FEEDBACK_NOT_FOUND));

        final var volunteer = feedback.getVolunteer();

        volunteer.removeFeedback(feedback);

        volunteerRepository.saveAndFlush(volunteer);
        volunteerFeedbackRepository.delete(feedback);
    }

    private VolunteerDTO mapVolunteerToDTO(final Volunteer volunteer) {
        return VolunteerDTO.builder()
                .id(volunteer.getId())
                .createDate(volunteer.getCreateDate())
                .updateDate(volunteer.getUpdateDate())
                .reputationScore(volunteer.getReputationScore())
                .firstName(volunteer.getUser() == null ? null : volunteer.getUser().getFirstName())
                .lastName(volunteer.getUser() == null ? null : volunteer.getUser().getLastName())
                .volunteerFeedbacks(volunteer.getVolunteerFeedbacks() == null ? List.of() :
                        volunteer.getVolunteerFeedbacks()
                                .stream()
                                .map(vf ->
                                        VolunteerDTO.VolunteerFeedbackDTO.builder()
                                                .id(vf.getId())
                                                .createDate(vf.getCreateDate())
                                                .updateDate(vf.getUpdateDate())
                                                .text(vf.getText())
                                                .reputationScore(vf.getReputationScore())
                                                .author(getAuthor(vf))
                                                .build()
                                )
                                .toList()
                )
                .build();
    }

    private String getAuthor(final VolunteerFeedback vf) {
        return vf.getUser() == null ? null : vf.getUser().getEmail();
    }
}
