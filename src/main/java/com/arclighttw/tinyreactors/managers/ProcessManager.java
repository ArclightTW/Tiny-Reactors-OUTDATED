package com.arclighttw.tinyreactors.managers;

import java.util.Iterator;
import java.util.List;

import com.arclighttw.tinyreactors.processes.IProcess;
import com.google.common.collect.Lists;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ProcessManager
{
	private static List<IProcess> processes = Lists.newArrayList();
	private static List<IProcess> newProcesses = Lists.newArrayList();
	
	public static void register()
	{
		MinecraftForge.EVENT_BUS.register(new ProcessManager());
	}
	
	@SubscribeEvent
	public void onServerTick(TickEvent.ServerTickEvent event)
	{
		if(event.phase != TickEvent.Phase.START)
			return;
		
		Iterator<IProcess> i = processes.iterator();
		
		while(i.hasNext())
		{
			IProcess process = i.next();
			
			if(process.isDead())
				i.remove();
			else
				process.update();
		}
		
		if(!newProcesses.isEmpty())
		{
			processes.addAll(newProcesses);
			newProcesses.clear();
		}
	}

	public static <T extends IProcess> T add(T process)
	{
		newProcesses.add(process);
		return process;
	}
}
