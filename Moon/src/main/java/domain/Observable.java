package domain;

import java.util.ArrayList;
import java.util.List;

public abstract class Observable {

    private final List<Observer> observers = new ArrayList<>();

    private final List<NotificationsObserver> notificationsObservers = new ArrayList<>();

    protected void notifyObservers() {
        for (Observer observer : observers)
            observer.update();
    }

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    protected void notifyNotificationObservers(Notification notification) {
        for (NotificationsObserver observer : notificationsObservers)
            observer.updateNotifications(notification);
    }

    public void addNotificationObserver(NotificationsObserver notificationsObserver) {
        notificationsObservers.add(notificationsObserver);
    }

    protected void notifyNotificationObserversAboutRead() {
        for (NotificationsObserver observer : notificationsObservers)
            observer.updateReadStatus();
    }

}
