package ui;

import business.Service;
import javafx.stage.Stage;

import java.awt.*;
import java.util.*;
import java.util.List;

public class ScreenController{

    private static Service service;
    public static void setService(Service injectedService) {
        service = injectedService;
        List<ApplicationStages> notifiableStages = new ArrayList<>(Arrays.asList(ApplicationStages.EVENT,
                ApplicationStages.MAIN, ApplicationStages.MESSENGER));
        for (ApplicationStages stage : notifiableStages)
            availableStages.put(stage, ApplicationStages.getStage(stage, service));
    }

    private static Map<ApplicationStages, Stage> availableStages = new HashMap<>();
    static {
        for (ApplicationStages stage : ApplicationStages.values())
            availableStages.put(stage, null);
    }

    private static Stack<ApplicationStages> stagesStack = new Stack<>();

    public static void openPage(ApplicationStages stage) {
        if (availableStages.get(stage) == null) {
            availableStages.put(stage, ApplicationStages.getStage(stage, service));
        }
        hideTop();
        if (!stagesStack.isEmpty()) {
            double x = availableStages.get(stagesStack.peek()).getX();
            double y = availableStages.get(stagesStack.peek()).getY();
            stagesStack.add(stage);
            showTop(x, y);
        } else {
            stagesStack.add(stage);
            showTop();
        }
    }

    public static void goBack() {
        hideTop();
        double x = availableStages.get(stagesStack.peek()).getX();
        double y = availableStages.get(stagesStack.peek()).getY();
        stagesStack.pop();
        showTop(x, y);
    }

    private static void hideTop() {
        if (!stagesStack.isEmpty()) {
            availableStages.get(stagesStack.peek()).hide();
        }
    }

    private static void showTop(double x, double y) {
        if (!stagesStack.isEmpty()) {
            availableStages.get(stagesStack.peek()).setX(x);
            availableStages.get(stagesStack.peek()).setY(y);
            showTop();
        }
    }

    private static void showTop() {
        if (!stagesStack.isEmpty()) {
            availableStages.get(stagesStack.peek()).show();
        }
    }

    public static Stage getCurrentStage() {
        if (!stagesStack.isEmpty()) {
            return availableStages.get(stagesStack.peek());
        }
        return null;
    }

    public static void closeApplication() {
        hideTop();
        availableStages.values().forEach(s -> { if (s != null) s.close(); });
        stagesStack.clear();
        availableStages.clear();
        service.close();
    }
}

