package HxCKDMS.HxCShards.utils;

import hxckdms.hxcconfig.Config;

import java.util.HashMap;
import java.util.Iterator;

@Config
public class EntityList {
	public static HashMap<String, Boolean> wList = new HashMap<>();

	public void init() {
		Iterator<String> iter = EntityMapper.entityList.iterator();
		iter.forEachRemaining(i -> wList.putIfAbsent(i, true));
	}
}
