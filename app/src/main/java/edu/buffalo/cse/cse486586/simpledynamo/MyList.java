package edu.buffalo.cse.cse486586.simpledynamo;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by parik on 5/3/18.
 */

class MyList<E> extends ArrayList<E> {

    public MyList(){

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
