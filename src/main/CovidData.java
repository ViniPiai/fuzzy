package main;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Matrix with all convid tuples.
 *
 * @author andre | vinicius
 */
public class CovidData {

    private List<CovidTuple> covidTuples;

    public CovidData(String[][] data) {
        this.covidTuples = new LinkedList<>();
        initAttribute(data);
    }

    private void initAttribute(String[][] data) {
        for (String[] tuple : data) {
                addCovidTuple(tuple);        
        }
    }

    public CovidData addCovidTuple(String[] tuple) {
        covidTuples.add(new CovidTuple(tuple));
        return this;
    }

    public List<CovidTuple> getCovidTuples() {
        return covidTuples;
    }
    
    public void print(){
        System.out.println(Arrays.toString(covidTuples.toArray()));
    }
    
    public CovidTuple getTupleById(int id){
        return covidTuples.stream().filter(c -> c.getId() == id)
                .findFirst().orElse(null);
    }

}
