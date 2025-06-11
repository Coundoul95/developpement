package sn.afrilins.net.gestionEnquete.exception;

public class CustomBadRequestException extends RuntimeException {
    private BadRequestAlertException badRequest;

    public CustomBadRequestException(BadRequestAlertException badRequest) {
        super(badRequest.getMessage());
        this.badRequest = badRequest;
    }

    public BadRequestAlertException getBadRequest() {
        return badRequest;
    }
}