package domain;

import java.util.Arrays;
import java.util.Objects;

public class Tuple<ID>  {

    protected ID id1;
    protected ID id2;

    public Tuple(ID id1, ID id2) {
        this.id1 = id1;
        this.id2 = id2;
    }

    public ID getId1() {
        return id1;
    }

    public void setId1(ID id1) {
        this.id1 = id1;
    }

    public ID getId2() {
        return id2;
    }

    public void setId2(ID id2) {
        this.id2 = id2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tuple<?> tuple = (Tuple<?>) o;
        return Objects.equals(id1, tuple.id1) && Objects.equals(id2, tuple.id2) || Objects.equals(id1,tuple.id2) && Objects.equals(id2,tuple.id1);
    }

    @Override
    public int hashCode() {
        if(id1.getClass() == Integer.class && id2.getClass() == Integer.class){
            Integer a=(Integer) id1;
            Integer b=(Integer) id2;
            Integer[] list;
            if(a<b)
                list = new Integer[]{a,b};
            else
                list = new Integer[]{b,a};
            return Arrays.hashCode(list);
        }
        return Objects.hash(id1, id2);
    }
}
