package edu.buffalo.cse.cse486586.simpledynamo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by parik on 4/24/18.
 */

public class RingArrayList<E> extends ArrayList<E> {

    //Adding this so that this has object reference to this class in toString method
    public RingArrayList(){

    }


    public RingArrayList(Set<String> hs) {
        super(new ArrayList(hs));
    }

    @Override
    public E get(int i) {
        return super.get(i % size());
    }

    @Override
    public  String toString(){

        String out="";

        Iterator<E> it= this.iterator();

       while (it.hasNext()){
           out=out+it.next()+"--";

       }
       return out.substring(0,out.length()-2);
    }


}
