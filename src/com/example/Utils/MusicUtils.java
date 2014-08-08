package com.example.Utils;

import java.util.List;

import com.example.model.MusicInfo;

public class MusicUtils {
	public static int seekToPosById(List<MusicInfo> list, int id){
		if(list == null || id == -1)
			return -1;
		for(int i=0; i< list.size(); i++)
		{
			if(list.get(i).songid == id)
			{
				return i;
			}
		}
		return -1;
	}

}
