package web;

import javax.management.Notification;

public interface NotificationHandler {
    void notify(Notification notification);
}
