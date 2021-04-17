package com.main.climbingdiary.database.entities;

import android.database.Cursor;

import com.main.climbingdiary.common.preferences.AppPreferenceManager;
import com.main.climbingdiary.models.MenuValues;
import com.main.climbingdiary.database.TaskRepository;
import com.main.climbingdiary.models.Tabs;

import org.chalup.microorm.MicroOrm;

import java.util.ArrayList;

import lombok.Getter;

public class RouteRepository<T> {

    private final MicroOrm uOrm = new MicroOrm();
    private final Class<T> klass;
    @Getter
    private final TaskRepository taskRepository;

    public RouteRepository(Class<T> klass) {
        this.klass = klass;
        this.taskRepository = TaskRepository.getInstance();
    }

    public T getRoute(int _id) {
        Cursor cursor = null;
        if (klass == Route.class) {
            cursor = taskRepository.getRoute(_id);
        } else if (klass == Projekt.class) {
            cursor = taskRepository.getProjekt(_id);
        }
        return uOrm.fromCursor(cursor, klass);
    }

    public ArrayList<T> getRouteList() {
        ArrayList<T> _routes = new ArrayList<>();
        Cursor cursor = null;
        if (klass == Route.class) {
            cursor = taskRepository.getAllRoutes();
        } else if (klass == Projekt.class) {
            cursor = taskRepository.getAllProjekts();
        }
        if (cursor != null) {
            while (!cursor.isAfterLast()) {
                _routes.add(uOrm.fromCursor(cursor, klass));
                cursor.moveToNext();
            }
        }
        return _routes;
    }

    public ArrayList<T> getListByArea(int areaId) {
        AppPreferenceManager.setFilterSetter(MenuValues.FILTER);
        AppPreferenceManager.setFilter(String.format("g.id = %s", areaId));
        ArrayList<T> _routes = getRouteList();
        AppPreferenceManager.removeAllFilterPrefs();
        return _routes;
    }

    public boolean deleteRoute(RouteElement object) {
        if (object instanceof Projekt) {
            return taskRepository.deleteProjekt(((Projekt) object).getId());
        } else {
            return taskRepository.deleteRoute(((Route) object).getId());
        }
    }

    public boolean insertRoute(Object object) {
        if (object instanceof Route) {
            return taskRepository.insertRoute((Route) object);
        } else if (object instanceof Projekt) {
            return taskRepository.insertProjekt((Projekt) object);
        }else{
            return false;
        }
    }

    public boolean updateRoute(Object object){
        if (object instanceof Route) {
            return taskRepository.updateRoute((Route) object);
        } else if (object instanceof Projekt) {
            return taskRepository.insertProjekt((Projekt) object);
        }else{
            return false;
        }
    }
}
