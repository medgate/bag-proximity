package ch.bag.screening.domain.screening;

import ch.bag.screening.storage.screening.ImportException;
import ch.bag.screening.storage.screening.JsonScreeningTree;

public interface ScreeningParser {
  JsonScreeningTree readScreeningDecisionTree(String version) throws ImportException;
}
