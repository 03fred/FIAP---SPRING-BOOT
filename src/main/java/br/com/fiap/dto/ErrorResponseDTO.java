package br.com.fiap.dto;

public class ErrorResponseDTO {

	private final String message;
    private int statusCode;
    
	public ErrorResponseDTO(String message, int statusCode) {
	    this.message = message;
	    this.statusCode = statusCode;
	}


    public String getMessage() {
        return message;
    }


	public int getStatusCode() {
		return statusCode;
	}
    
}
