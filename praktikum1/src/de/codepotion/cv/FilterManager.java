package de.codepotion.cv;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import de.codepotion.cv.filters.ImageFilter;

public class FilterManager {
	private ClassLoader myLoader;
	private List<ImageFilter> availableFilters;
	private static FilterManager instance= null;
	
	public FilterManager()
	{
		availableFilters = new ArrayList<ImageFilter>();
		
		myLoader  = MainApp.class.getClassLoader();
	}
	
	public void updateFilters()
	{
		availableFilters.clear();
		
		//TODO: fix ugly hardcoded path
		File filterPath = new File("bin//de//codepotion//cv//filters//");
		String[] filenames = filterPath.list(new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String name) {
				if(name.endsWith(".class"))
					return true;
				else
					return false;
			}
		});
		
		for (String filename : filenames) {
			filename = filename.replace(".class", "");
				try {
					Class<?> temp = myLoader.loadClass(ImageFilter.class.getPackage().getName()+"."+filename);
					if(!temp.isAnonymousClass() && !temp.isInterface() && !Modifier.isAbstract(temp.getModifiers()))
					{
						availableFilters.add((ImageFilter)temp.newInstance());
					}
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	
	public List<ImageFilter> getFilters()
	{
		return availableFilters;
	}
	
	public static FilterManager getInstance()
	{
		if(instance == null)
		{
			instance = new FilterManager();
			instance.updateFilters();
		}
		return instance;
	}
	
}