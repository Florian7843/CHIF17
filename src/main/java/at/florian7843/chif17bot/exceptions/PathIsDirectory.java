package at.florian7843.chif17bot.exceptions;

public class PathIsDirectory extends Exception {

  public PathIsDirectory() {
    super();
  }

  public PathIsDirectory(String message) {
    super(message);
  }

  public PathIsDirectory(String message, Throwable cause) {
    super(message, cause);
  }

  public PathIsDirectory(Throwable cause) {
    super(cause);
  }

}
