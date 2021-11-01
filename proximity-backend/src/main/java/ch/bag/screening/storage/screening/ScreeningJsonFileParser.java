package ch.bag.screening.storage.screening;

import ch.bag.screening.domain.screening.ScreeningParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class ScreeningJsonFileParser implements ScreeningParser {

  private final ObjectMapper jsonMapper;

  /**
   * Parses the decision tree from the JSON file corresponding to the provided version.
   *
   * @param version the screening tree version
   * @throws ImportException if the versioned JSON file could not be parsed as expected
   * @return JsonScreeningTree the screening decision tree
   */
  @Override
  public JsonScreeningTree readScreeningDecisionTree(final String version) throws ImportException {
    final String filePath = getScreeningTreeFilePath(version);
    try (final InputStream input = new ClassPathResource(filePath).getInputStream()) {
      LOG.debug("Reading screening decision tree from {} JSON file", filePath);
      return jsonMapper.readValue(input, JsonScreeningTree.class);
    } catch (final Exception cause) {
      LOG.error("Import error while reading screening decision tree file {}", filePath, cause);
      throw new ImportException("Error while importing screening decision tree", cause);
    }
  }

  private static String getScreeningTreeFilePath(final String version) {
    return "screening/" + version + ".json";
  }
}
