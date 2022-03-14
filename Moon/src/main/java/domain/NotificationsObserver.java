package domain;

public interface NotificationsObserver {

    public void updateNotifications(Notification notification);

    public void updateReadStatus();
}
