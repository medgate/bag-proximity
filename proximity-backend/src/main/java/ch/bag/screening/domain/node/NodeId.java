package ch.bag.screening.domain.node;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.trimToEmpty;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
@AllArgsConstructor(staticName = "of")
public final class NodeId {

  private final String id;

  public static NodeId empty() {
    return NodeId.of(EMPTY);
  }

  public static NodeId fromString(final String id) {
    return NodeId.of(trimToEmpty(id));
  }

  public String get() {
    return id;
  }

  @Override
  public String toString() {
    return id;
  }
}
