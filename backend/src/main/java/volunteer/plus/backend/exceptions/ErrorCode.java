package volunteer.plus.backend.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    ADD_REQUEST_NOT_FOUND("Add request not found"),
    EMPTY_BRIGADE_DATA("Empty brigade data for create/update"),
    EMPTY_MILITARY_PERSONNEL_DATA("Empty military personnel data for create"),
    NOT_VALID_REGIMENT_CODE("Not valid regiment code"),
    BRIGADE_NOT_FOUND("Brigade not found"),
    ADD_REQUEST_IS_ALREADY_EXECUTED("Add request is already executed"),
    MILITARY_PERSONNEL_NOT_FOUND("Military personnel not found"),
    ONLY_ONE_CREATOR_FOR_REQUESTS("Only one creator for requests is allowed"),
    USER_NOT_FOUND("User not found"),
    LEVY_NOT_FOUND("Levy not found"),
    VOLUNTEER_NOT_FOUND("Volunteer not found"),
    REPORT_NOT_FOUND("Report not found"),
    RESOURCE_NOT_FOUND("Resource is not found"),
    FAILED_S3_UPLOAD("Failed upload for s3 bucket"),
    EMPTY_FILE("File is empty"),
    WRONG_FILE_FORMAT("File format is considered as wrong"),
    ATTACHMENT_NOT_FOUND("Attachment not found"),
    NO_DATA_FOR_REPORT_OPERATION("No data for report operation"),
    LEVY_ALREADY_HAS_A_REPORT("Levy already has a report"),
    CANNOT_DESERIALIZE_JSON("Cannot deserialize a json"),
    EMAIL_TEMPLATE_NOT_FOUND("Email template not found"),
    CANNOT_SERIALIZE_JSON("Cannot serialize data"),
    VOLUNTEER_IS_ALREADY_ASSIGNED_TO_USER("Volunteer is already assigned to user"),
    VOLUNTEER_FEEDBACK_NOT_FOUND("Volunteer feedback not found"),
    CANNOT_DOWNLOAD_OUTPUT_FILE("Cannot download output file");
    private final String data;
}
