package br.com.fiap.dto;

import java.util.List;

public record ValidationErrorDto (List<String> errors, int httpStatusCode){

}
