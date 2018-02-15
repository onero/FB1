package easv.fb1;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ole on 14/02/2018.
 */

public class BEFriend {

String m_name;
String m_phone;

public BEFriend(){}

public BEFriend(Map<String, Object> map)
{
    m_name = (String)map.get("name");
    m_phone = (String)map.get("phone");
}

public BEFriend(String name, String phone)
{ m_name = name; m_phone = phone;}

public String getName(){ return m_name; }
public String getPhone() { return m_phone; }

public Map<String, Object> toMap()
{
    Map<String, Object> res = new HashMap<>();
    res.put("name", getName());
    res.put("phone", getPhone());
    return res;
}
}
