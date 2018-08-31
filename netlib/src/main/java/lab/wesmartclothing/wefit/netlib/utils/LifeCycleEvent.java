package lab.wesmartclothing.wefit.netlib.utils;

public enum LifeCycleEvent {
    CREATE,
    START,
    RESUME,
    PAUSE,
    STOP,
    RESTART,
    DESTROY,

    // Fragment Events
    ATTACH,
    CREATE_VIEW,
    DESTROY_VIEW,
    DETACH
}