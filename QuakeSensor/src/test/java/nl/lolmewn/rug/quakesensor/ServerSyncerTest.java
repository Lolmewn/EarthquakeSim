package nl.lolmewn.rug.quakesensor;

import java.util.HashSet;
import java.util.Set;
import nl.lolmewn.rug.quakecommon.net.ServerAddress;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Lolmewn
 */
public class ServerSyncerTest {

    @Test
    public void testCombine() {
        /*
         * This test checks the combining of two sets as done in ServerSyncer.
         * Two sets being combined should produce the union of the two sets.
         */
        Set<ServerAddress> set = new HashSet<>();
        set.add(new ServerAddress("localhost", 5000));
        set.add(new ServerAddress("localhost", 5001));
        
        Set<ServerAddress> addition = new HashSet<>();
        set.add(new ServerAddress("localhost", 5000));
        set.add(new ServerAddress("localhost", 5002));
        
        set.addAll(addition);
        assertEquals(set.size(), 3); // 5000, 5001, 5002. Common 5000 is left out
    }
    
}
