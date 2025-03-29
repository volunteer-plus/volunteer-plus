package volunteer.plus.backend.service.general.impl;

import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import volunteer.plus.backend.domain.dto.ConversationRoomDTO;
import volunteer.plus.backend.domain.dto.UserConversationDTO;
import volunteer.plus.backend.domain.dto.WSMessageDTO;
import volunteer.plus.backend.domain.entity.ConversationRoom;
import volunteer.plus.backend.domain.entity.User;
import volunteer.plus.backend.domain.entity.WSMessage;
import volunteer.plus.backend.exceptions.ApiException;
import volunteer.plus.backend.exceptions.ErrorCode;
import volunteer.plus.backend.repository.ConversationRoomRepository;
import volunteer.plus.backend.repository.UserRepository;
import volunteer.plus.backend.repository.WSMessageRepository;
import volunteer.plus.backend.service.general.ConversationRoomService;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConversationRoomServiceImpl implements ConversationRoomService {
    private final ConversationRoomRepository conversationRoomRepository;
    private final UserRepository userRepository;
    private final WSMessageRepository wsMessageRepository;

    @Override
    public List<ConversationRoomDTO> findConversationRooms(final Long userId) {
        List<ConversationRoom> allRooms = conversationRoomRepository.findAllByDeletedFalse();

        if (userId != null) {
            allRooms = allRooms.stream()
                    .filter(room -> (room.getUsers() != null) && room.getUsers().stream().anyMatch(user -> Objects.equals(user.getId(), userId)))
                    .toList();
        }

        return allRooms.stream()
                .map(this::mapConversationRoomToDTO)
                .toList();
    }

    @Override
    public ConversationRoomDTO getConversationRoom(final Long conversationRoomId) {
        final ConversationRoom conversationRoom = findConversationRoom(conversationRoomId);
        return mapConversationRoomToDTO(conversationRoom);
    }

    @Override
    @Transactional
    public ConversationRoomDTO createConversationRoom(final ConversationRoomDTO conversationRoomDTO) {
        final ConversationRoom conversationRoom = ConversationRoom.builder()
                .name(conversationRoomDTO.getName())
                .deleted(false)
                .messages(new ArrayList<>())
                .users(new ArrayList<>())
                .build();

        if (conversationRoomDTO.getUsers() != null && !conversationRoomDTO.getUsers().isEmpty()) {
            final Set<Long> userIds = conversationRoomDTO.getUsers()
                    .stream()
                    .map(UserConversationDTO::getId)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            final List<User> users = userRepository.findAllById(userIds);

            if (userIds.size() != users.size()) {
                throw new ApiException(ErrorCode.USER_NOT_FOUND);
            }

            users.forEach(conversationRoom::addUser);
        }

        return mapConversationRoomToDTO(conversationRoomRepository.save(conversationRoom));
    }

    @Override
    @Transactional
    public ConversationRoomDTO addUserToConversationRoom(final Long conversationRoomId,
                                                         final Long userId) {
        final ConversationRoom conversationRoom = findConversationRoom(conversationRoomId);
        final User user = getUser(userId);

        conversationRoom.addUser(user);

        return mapConversationRoomToDTO(conversationRoomRepository.save(conversationRoom));
    }

    @Override
    @Transactional
    public ConversationRoomDTO removeUserFromConversationRoom(final Long conversationRoomId,
                                                              final Long userId) {
        final ConversationRoom conversationRoom = findConversationRoom(conversationRoomId);
        final User user = getUser(userId);

        conversationRoom.removeUser(user);

        return mapConversationRoomToDTO(conversationRoomRepository.save(conversationRoom));
    }

    @Override
    @Transactional
    public void deleteConversationRoom(final Long conversationRoomId) {
        final ConversationRoom conversationRoom = conversationRoomRepository.findById(conversationRoomId)
                .orElseThrow(() -> new ApiException(ErrorCode.CONVERSATION_ROOM_NOT_FOUND));

        conversationRoom.setDeleted(true);

        conversationRoomRepository.save(conversationRoom);
    }

    @Override
    @Transactional
    public void deleteConversationRoomMessage(final Long conversationRoomId,
                                              final Long messageId) {
        final ConversationRoom conversationRoom = findConversationRoom(conversationRoomId);

        final WSMessage message = wsMessageRepository.findById(messageId)
                .orElseThrow(() -> new ApiException(ErrorCode.MESSAGE_NOT_FOUND));

        conversationRoom.removeMessage(message);

        conversationRoomRepository.save(conversationRoom);
    }

    private User getUser(final Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
    }

    private ConversationRoom findConversationRoom(final Long conversationRoomId) {
        return conversationRoomRepository.findByIdAndDeletedFalse(conversationRoomId)
                .orElseThrow(() -> new ApiException(ErrorCode.CONVERSATION_ROOM_NOT_FOUND));
    }


    private ConversationRoomDTO mapConversationRoomToDTO(final ConversationRoom conversationRoom) {
        return ConversationRoomDTO.builder()
                .id(conversationRoom.getId())
                .name(conversationRoom.getName())
                .deleted(conversationRoom.isDeleted())
                .users(getConversationDTOS(conversationRoom))
                .messages(getWsMessageDTOS(conversationRoom))
                .build();
    }

    private List<UserConversationDTO> getConversationDTOS(final ConversationRoom conversationRoom) {
        return conversationRoom.getUsers() == null ?
                new ArrayList<>() :
                conversationRoom.getUsers()
                        .stream()
                        .map(user ->
                                UserConversationDTO.builder()
                                        .id(user.getId())
                                        .email(user.getEmail())
                                        .firstName(user.getFirstName())
                                        .lastName(user.getLastName())
                                        .build()
                        )
                        .toList();
    }

    private List<WSMessageDTO> getWsMessageDTOS(final ConversationRoom conversationRoom) {
        return conversationRoom.getMessages() == null ?
                new ArrayList<>() :
                conversationRoom.getMessages()
                        .stream()
                        .map(message ->
                                WSMessageDTO.builder()
                                        .id(message.getId())
                                        .fromUser(message.getFromUser())
                                        .content(message.getContent())
                                        .createDate(message.getCreateDate())
                                        .updateDate(message.getUpdateDate())
                                        .build()
                        )
                        .sorted(Comparator.comparing(WSMessageDTO::getCreateDate))
                        .toList();
    }
}
