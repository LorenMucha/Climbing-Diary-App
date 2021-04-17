package com.main.climbingdiary.testhelper;

import com.main.climbingdiary.common.preferences.AppPreferenceManager;
import com.main.climbingdiary.database.entities.Projekt;
import com.main.climbingdiary.database.entities.Route;
import com.main.climbingdiary.database.entities.RouteRepository;
import com.main.climbingdiary.models.Levels;
import com.main.climbingdiary.models.SportType;
import com.main.climbingdiary.models.Styles;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class TestHelper {

    private final RouteRepository routeRepository;
    private final RouteRepository projektRepository;
    private final int routeCount = 100;

    public TestHelper() {
        routeRepository = new RouteRepository<>(Route.class);
        projektRepository = new RouteRepository<>(Projekt.class);
    }

    public ArrayList getRouteList() {
        return routeRepository.getRouteList();
    }

    public ArrayList getProjektList() {
        return projektRepository.getRouteList();
    }

    public boolean createTestSources() {
        boolean state = false;
        for (SportType type : SportType.values()) {
            AppPreferenceManager.setSportType(type);
            for (Route route : createRouteList()) {
                state = routeRepository.insertRoute(route);
            }
            for (Projekt route : createProjektList()) {
                state = projektRepository.insertRoute(route);
            }
        }
        return state;
    }

    public Route createTestRoute(int i) {
        Route route = new Route();
        route.setId(i);
        route.setDate(randomDate());
        route.setComment(UUID.randomUUID().toString());
        route.setRating(new Random().nextInt(5));
        route.setArea(UUID.randomUUID().toString());
        route.setSector(UUID.randomUUID().toString());
        route.setLevel(Levels.getLevelsFrench()[new Random().nextInt(Levels.getLevelsFrench().length)]);
        route.setStyle(Styles.getStyle(true)[new Random().nextInt(Styles.getStyle(true).length)]);
        route.setName(UUID.randomUUID().toString());
        return route;
    }

    public Projekt createProjekt(int i) {
        Projekt route = new Projekt();
        route.setId(i);
        route.setComment(UUID.randomUUID().toString());
        route.setRating(new Random().nextInt(5));
        route.setArea(UUID.randomUUID().toString());
        route.setSector(UUID.randomUUID().toString());
        route.setLevel(Levels.getLevelsFrench()[new Random().nextInt(Levels.getLevelsFrench().length)]);
        route.setName(UUID.randomUUID().toString());
        return route;
    }

    private String randomDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(new Date(ThreadLocalRandom.current().nextInt() * 1000L));
    }

    private List<Route> createRouteList() {
        List<Route> routeList = new ArrayList<>();
        for (int i = 0; i <= routeCount; i++) {
            routeList.add(createTestRoute(i));
        }
        return routeList;
    }

    private List<Projekt> createProjektList() {
        List<Projekt> projektList = new ArrayList<>();
        for (int i = 0; i <= routeCount; i++) {
            projektList.add(createProjekt(i));
        }
        return projektList;
    }

    public boolean clearDb() {
        final String[] tasks = {
                getDeleteTask("routen_" + SportType.KLETTERN.typeToString()),
                getDeleteTask("routen_" + SportType.BOULDERN.typeToString()),
                getDeleteTask("projekte_" + SportType.KLETTERN.typeToString()),
                getDeleteTask("projekte_" + SportType.BOULDERN.typeToString()),
                getDeleteTask("sektoren_" + SportType.KLETTERN.typeToString()),
                getDeleteTask("sektoren_" + SportType.BOULDERN.typeToString()),
                getDeleteTask("gebiete_" + SportType.KLETTERN.typeToString()),
                getDeleteTask("gebiete_" + SportType.BOULDERN.typeToString())
        };
        return routeRepository.getTaskRepository().executeSqlTasks(tasks);
    }

    private String getDeleteTask(String tableName) {
        return "delete from " + tableName;
    }

    public boolean checkIfRouteNameExistsInList(String routenName) {
        boolean matches = false;
        for (Object o : this.getRouteList()) {
            Route r = (Route) o;
            if (r.getName().equals(routenName)) {
                matches = true;
                break;
            }
        }
        return matches;
    }

}