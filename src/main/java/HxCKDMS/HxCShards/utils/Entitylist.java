package HxCKDMS.HxCShards.utils;

import hxckdms.hxcconfig.Config;

import java.util.HashMap;
import java.util.Iterator;

@Config
public class Entitylist {
	public static HashMap<String, Boolean> wList = new HashMap<>();

	public void init() {
		Iterator<String> iter = EntityMapper.entityList.iterator();
		iter.forEachRemaining(i -> wList.putIfAbsent(i, true));
	}
}
