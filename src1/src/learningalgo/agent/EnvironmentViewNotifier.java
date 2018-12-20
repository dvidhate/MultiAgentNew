package learningalgo.agent;

/**
 *
 *
 */
public interface EnvironmentViewNotifier {

    /**
     * A simple notification message, to be forwarded to an Environment's
     * registered EnvironmentViews.
     *
     * @param msg the message to be forwarded to the EnvironmentViews.
     */
    void notifyViews(String msg);
}
