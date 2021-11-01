package ch.bag.screening.domain.profile;

import java.util.stream.Stream;
import lombok.Getter;

public enum SwissCanton {
  ARGOVIA("AG"),
  APPENZELL_INNER_RHODES("AI"),
  APPENZELL_OUTER_RHODES("AR"),
  BERNE("BE"),
  BASLE_COUNTRY("BL"),
  BASLE_CITY("BS"),
  FRIBOURG("FR"),
  GENEVA("GE"),
  GLARIS("GL"),
  GRISONS("GR"),
  JURA("JU"),
  LUCERNE("LU"),
  NEUCHATEL("NE"),
  NIDWALD("NW"),
  OBWALD("OW"),
  ST_GALL("SG"),
  SCHAFFHOUSE("SH"),
  SOLEURE("SO"),
  SCHWYZ("SZ"),
  THURGOVIA("TG"),
  TICINO("TI"),
  URI("UR"),
  VAUD("VD"),
  VALAIS("VS"),
  ZUG("ZG"),
  ZURICH("ZH"),

  // Technically Liechtenstein is a country, not a Swiss canton \o/
  LIECHTENSTEIN("LI");

  @Getter private final String code;

  SwissCanton(final String code) {
    this.code = code;
  }

  public static SwissCanton fromCode(final String cantonCode) {
    return Stream.of(values())
        .filter(canton -> canton.getCode().equalsIgnoreCase(cantonCode))
        .findFirst()
        .orElse(null);
  }
}
