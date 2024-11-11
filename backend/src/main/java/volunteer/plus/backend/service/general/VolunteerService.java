package volunteer.plus.backend.service.general;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import volunteer.plus.backend.domain.dto.VolunteerDTO;
import volunteer.plus.backend.domain.dto.VolunteerFeedbackPayloadDTO;

import java.util.Set;


public interface VolunteerService {
    Page<VolunteerDTO> getVolunteers(Pageable pageable, Set<Long> volunteerIds);

    VolunteerDTO createVolunteer(String userEmail);

    VolunteerDTO createOrUpdateFeedback(String userEmail, VolunteerFeedbackPayloadDTO volunteerFeedbackPayloadDTO);

    void removeFeedback(Long feedbackId);
}
