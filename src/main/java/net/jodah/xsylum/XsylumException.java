package net.jodah.xsylum;

public class XsylumException extends Exception {
  private static final long serialVersionUID = -3246260520113823143L;

  public XsylumException(Throwable c, String format, Object... args) {
    super(String.format(format, args), c);
  }

  public XsylumException(String format, Object... args) {
    super(String.format(format, args));
  }
}