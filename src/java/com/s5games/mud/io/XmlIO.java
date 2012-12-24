package com.s5games.mud.io;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.s5games.mud.beans.container.CommandSet;
import com.s5games.mud.beans.container.ScriptSet;
import com.s5games.mud.beans.container.WorldSet;
/**
 * 
 * @author George Frick (george.frick@gmail.com)
 *
 */
public class XmlIO {

	public static ScriptSet readScripts(File file) {
		ScriptSet set = null;
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(ScriptSet.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			set = (ScriptSet) jaxbUnmarshaller.unmarshal(file);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return set;
	}

	public static void writeScripts(File file, ScriptSet set) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(ScriptSet.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(set, file);
			jaxbMarshaller.marshal(set, System.out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void writeWorlds(File file, WorldSet worlds) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(WorldSet.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(worlds, file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static WorldSet readWorlds(File file) {
		WorldSet worlds = null;
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(WorldSet.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			worlds = (WorldSet) jaxbUnmarshaller.unmarshal(file);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return worlds;
	}

	public static CommandSet readCommands(File file) {
		CommandSet commands = null;
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(CommandSet.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			commands = (CommandSet) jaxbUnmarshaller.unmarshal(file);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return commands;
	}

}
