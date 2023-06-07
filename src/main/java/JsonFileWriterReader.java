

import java.io.*;
import java.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.core.type.TypeReference;
import org.tinylog.Logger;

/**
 * A singleton class that provides writing and reading functions for the data.
 */
public class JsonFileWriterReader {
    private File file = new File("gameStat.json");
    private ObjectMapper mapper = new ObjectMapper();
    private static JsonFileWriterReader writerReader = new JsonFileWriterReader();
    private JsonFileWriterReader(){
        mapper.registerModule(new JavaTimeModule());
    }

    /**
     * Singleton representation of the file writer.
     * @return single static object for writing and reading JSON file.
     */
    public static JsonFileWriterReader getInstance(){
        return writerReader;
    }

    /**
     * Creates a json file with list of game information. If the file already exists, the list is appended with the new infromation.
     * @author Altan Dzhumaev
     * @param info a GameInfo object that contains information about game that will be stored in json
     */
    public void appendToList(GameInfo info){
        try{
            List<GameInfo> statisticsList = new ArrayList<GameInfo>();
            if(file.exists()) {
                FileReader reader = new FileReader(file);
                statisticsList = mapper.readValue(reader,new TypeReference<ArrayList<GameInfo>>(){});
            }
            FileWriter writer = new FileWriter(file);
            statisticsList.add(info);
            mapper.writeValue(writer,statisticsList);
            Logger.info("Writing to the file");
        }catch (Exception e){
            Logger.error(e.getStackTrace());
        }
    }

    /**
     * Traverses json file and build a list of 5 players with the most scores.
     * @author Altan Dzhumaev
     * @return key value list pair that contains top 5 players with most wins
     */
    public List<Map.Entry<String,Integer>> getTopScoreList(){
        Map<String,Integer> statisticsMap = new HashMap<>();
        try(FileReader reader = new FileReader(file)){
            List<GameInfo> statisticsList = new ArrayList<GameInfo>();
            statisticsList = mapper.readValue(reader,new TypeReference<ArrayList<GameInfo>>(){});
            statisticsList.stream().forEach(element->{
                if(statisticsMap.containsKey(element.getWinner())) {
                    statisticsMap.put(element.getWinner(), Integer.valueOf(statisticsMap.get(element.getWinner()) + Integer.valueOf(1)));
                }else{
                    statisticsMap.put(element.getWinner(), Integer.valueOf(Integer.valueOf(1)));
                }
            });

        }catch(Exception e){
            Logger.error(e.getStackTrace());
        }
        return sortMap(statisticsMap).stream().limit(5).toList();
    }
    private List<Map.Entry<String,Integer>> sortMap(Map<String,Integer> map){
        List<Map.Entry<String,Integer>> list = new ArrayList<Map.Entry<String,Integer>>(map.entrySet());
        Collections.sort(list,(firstElement,secondElement)->{
            return secondElement.getValue().compareTo(firstElement.getValue());
        });
        return list;
    }


}
