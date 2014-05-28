/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.gbif.bourgogne;

import java.nio.ByteBuffer;
import java.util.UUID;

/**
 * Class for generating Persistent Identifiers
 * @author korbinus
 */
public class PID {
    private UUID _uuid;
    
    /**
     * Constructor generating a random UUID
     */
    public PID() {
        _uuid = UUID.randomUUID();
    }
    
    /**
     * Constructor generating an UUID from a binary parameter
     * @param data 
     */
    public PID(byte[] data) {
        setUUID(data);
    }
    
    @Override
    public String toString() {
        return _uuid.toString();
    }
    
    public UUID getUUID() {
        return _uuid;
    }
    
    public void setUUID(byte[] data) {
        ByteBuffer bb = ByteBuffer.wrap(data);
        _uuid = new UUID(bb.getLong(), bb.getLong());        
    }
    
    /**
     * Returns PID as binary
     * @return 
     */
    public byte[] toBytes() {
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(_uuid.getMostSignificantBits());
        bb.putLong(_uuid.getLeastSignificantBits());
        return bb.array();
    }
}
