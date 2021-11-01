package ch.bag.screening.domain.error;

public interface LocalizedError {

  String getLocalizedMessage();

  String getDefaultMessage();
}
