package repository.memoryRepo;

import domain.Entity;
import repository.Repository;

import java.util.HashMap;
import java.util.Map;

public class InMemoryRepository<ID,E extends Entity<ID>> implements Repository<ID,E>{

    protected Map<ID,E> entities;

    public InMemoryRepository() {
        this.entities = new HashMap<>();
    }

    @Override
    public E findOne(ID id) {
        if(id==null)
            throw new IllegalArgumentException("Id must not be null\n");
        return entities.get(id);
    }

    @Override
    public Iterable<E> findAll() {
        return entities.values();
    }

    @Override
    public E save(E entity) {
        if(entity==null)
            throw new IllegalArgumentException("Null entity\n");

        if(entities.get(entity.getId()) != null)
            return entity;
        entities.put(entity.getId(),entity);
        return null;
    }

    @Override
    public E delete(E entity) {
        if(entity==null)
            throw new IllegalArgumentException("Null entity");

        if(entities.get(entity.getId())!=null) {
            entities.remove(entity.getId(), entity);
            return null;
        }

        return entity;
    }

    @Override
    public E update(E entity) {
        return null;
    }

    @Override
    public void syncContent(){

    }
}
