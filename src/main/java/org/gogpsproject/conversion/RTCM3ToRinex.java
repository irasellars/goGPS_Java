/*
 * Copyright (c) 2011 Eugenio Realini, Mirko Reguzzoni, Cryms sagl - Switzerland. All Rights Reserved.
 *
 * This file is part of goGPS Project (goGPS).
 *
 * goGPS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * goGPS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with goGPS.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.gogpsproject.conversion;

import java.io.File;
import java.util.Locale;

import org.gogpsproject.producer.parser.rtcm3.RTCM3FileReader;
import org.gogpsproject.producer.rinex.RinexV2Producer;

/**
 * @author Lorenzo Patocchi, cryms.com
 *
 * Converts NVS binary file to RINEX
 *
 */
public class RTCM3ToRinex {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		//force dot as decimal separator
		Locale.setDefault(new Locale("en", "US"));
		
		boolean singleFreq = false;
		boolean needApproxPos = true;

		if(args.length<3){
			System.out.println("RTCM3ToRinex <RTCM3 file> <marker name> <starting GPS week> [<min DOY>]");
			return;
		}

		int p=0;
		String inFile = args[p++];
		String marker = args[p++];
		int week =Integer.parseInt(args[p++]);
		int minDOY = 0;
		if (args.length>3)
			minDOY = Integer.parseInt(args[p++]);

		System.out.println("in :"+inFile);
		
		RinexV2Producer rp = new RinexV2Producer(needApproxPos, singleFreq, marker, minDOY);

		RTCM3FileReader masterIn = new RTCM3FileReader(new File(inFile), week);
		try {
			masterIn.init();
			masterIn.addStreamEventListener(rp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		while(masterIn.hasMoreObservations()){
			masterIn.getNextObservations();
		}
		
		rp.streamClosed();
		System.out.println("END");
	}
}
