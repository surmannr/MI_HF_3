import java.util.Arrays;
import java.util.OptionalDouble;
import java.util.Random;

public class FlappyAgent {
    static class StateDTO {
        public int birdPos;
        public int birdSpeed;
        public int tubeDistance;
        public int tubeHeight;

        public StateDTO(int birdPos, int birdSpeed, int tubeDistance, int tubeHeight) {
            this.birdPos = birdPos;
            this.birdSpeed = birdSpeed;
            this.tubeDistance = tubeDistance;
            this.tubeHeight = tubeHeight;
        }

        @Override
        public String toString() {
            return "StateDTO{" +
                    "birdPos=" + birdPos +
                    ", birdSpeed=" + birdSpeed +
                    ", tubeDistance=" + tubeDistance +
                    ", tubeHeight=" + tubeHeight +
                    '}';
        }
    }
    static class QTable implements java.io.Serializable {
        public double[][][][][] table;

        public QTable() {

        }

        public QTable(int[] stateSpaceSize, int actionDimension) {
            table = new double[stateSpaceSize[0]][stateSpaceSize[1]][stateSpaceSize[2]][stateSpaceSize[3]][actionDimension];
        }

        public double[] getActions(FlappyAgent.StateDTO state) {
            return table[state.birdPos][state.birdSpeed][state.tubeDistance][state.tubeHeight];
        }

        public FlappyAgent.QTable copy() {
            FlappyAgent.QTable res = new FlappyAgent.QTable();
            res.table = this.table.clone();
            return res;
        }
    }

    QTable qTable;
    int[] actionSpace;
    int nIterations;

    boolean test = false;

    public FlappyAgent(int[] observationSpaceSize, int[] actionSpace, int nIterations) {
        this.qTable = new QTable(observationSpaceSize,actionSpace.length);
        this.actionSpace = actionSpace;
        this.nIterations = nIterations;
    }
    public int argmax(double[] array){


        int best = -1;
        double best_confidence = -1000.0;

        for(int i = 0;i < array.length;i++){

            double value = array[i];

            if (value > best_confidence){

                best_confidence = value;
                best = i;
            }
        }

        return best;


    }
    public int step(StateDTO state) {
        int action = 0;//
        double epsilon = 0.008;
        Random rand = new Random();
        double randomszam = rand.nextDouble();
        if(test){
             action = argmax(qTable.getActions(state));
        } else{
            if(randomszam>epsilon){
               action = argmax(qTable.getActions(state));
            } else {
                action = rand.nextInt(2);
            }

        }
        return action;
    }
    public void epochEnd(int epochRewardSum) {

    }
    public void learn(StateDTO oldState, int action, StateDTO newState, int reward) {
        double alpha = 0.8;
        double discount_factor = 0.95;

        double[] asd = qTable.getActions(newState);
        double maxQ_value = Math.max(asd[0], asd[1]);
        //OptionalDouble maxQ_value = Arrays.stream(qTable.getActions(newState)).max();
        //System.out.println("MAX Q ERTEKE: " + maxQ_value);

        qTable.getActions(oldState)[action] = (1-alpha) * qTable.getActions(oldState)[action] + alpha * (reward + discount_factor * maxQ_value);
    }
    public void trainEnd() {
        /*...*/
        test = true;
    }
}
