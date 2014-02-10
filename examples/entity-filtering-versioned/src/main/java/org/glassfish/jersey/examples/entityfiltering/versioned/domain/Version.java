package org.glassfish.jersey.examples.entityfiltering.versioned.domain;

import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * A version string - captures raw version and parses into an array of integers
 * representing individual version numbers, eg: major.minor.fix.build (eg:
 * 2.0.1.52)
 * 
 * @author Andy Pemberton (pembertona at gmail.com)
 * 
 */
public class Version implements Comparable<Version> {

	/**
	 * The raw, textual version number (eg: "02.00.01.52")
	 */
	private String rawVersion;

	/**
	 * The individual version number parts, if they can be extracted (eg: [2, 0,
	 * 1, 52]
	 */
	private int[] versionParts;

	/**
	 * Regular Expression representing non-numeric separators between version
	 * numbers; treats any non-digit as a separator, thus supports period
	 * notation (eg: 1.2) or other syntaxes (eg: 1-2-3, 1.2-3).
	 * 
	 */
	private static Pattern SEPARATOR_PATTERN = Pattern.compile("(\\D+)");

	public Version(String version) {
		this.rawVersion = version;

		if (Versioned.BEGINNING_OF_TIME.equals(version)) {
			this.versionParts = new int[1];
			this.versionParts[0] = Integer.MIN_VALUE;
		} else if (Versioned.END_OF_TIME.equals(version)) {
			this.versionParts = new int[1];
			this.versionParts[0] = Integer.MAX_VALUE;
		} else {
			String[] versionPartsStrings = SEPARATOR_PATTERN.split(version);

			if (versionPartsStrings.length > 0) {
				this.versionParts = new int[versionPartsStrings.length];
				int i = 0;
				for (String versionPart : versionPartsStrings) {
					this.versionParts[i++] = Integer.valueOf(versionPart);
				}
			}
		}
	}

	public String getRawVersion() {
		return rawVersion;
	}

	public void setRawVersion(String rawVersion) {
		this.rawVersion = rawVersion;
	}

	public int[] getVersionParts() {
		return versionParts;
	}

	public void setVersionParts(int[] versionParts) {
		this.versionParts = versionParts;
	}

	public int compareTo(Version that) {
		int compare = 0;

		int[] thisOne = this.getVersionParts();
		int[] thatOne = that.getVersionParts();

		if (thatOne != null && thisOne != null) {
			// normalize array lengths
			if (thisOne.length < thatOne.length) {
				thisOne = Arrays.copyOf(thisOne, thatOne.length);
			}
			if (thatOne.length < thisOne.length) {
				thatOne = Arrays.copyOf(thatOne, thisOne.length);
			}

			// comparing 2 numeric versions
			for (int i = 0; i < thisOne.length; i++) {
				if (thatOne[i] != thisOne[i]) {
					compare = Double.compare(thisOne[i], thatOne[i]);
					break;
				}
			}
		} else if (thatOne == null && thisOne == null) {
			// comparing 2 text-only versions
			compare = this.rawVersion.compareTo(that.rawVersion);
		} else {
			throw new UnsupportedOperationException("Cannot compare numeric to text-only Versions.");
		}

		return compare;
	}

	public boolean isLessThan(Version that) {
		return this.compareTo(that) < 0;
	}

	public boolean isGreaterThan(Version that) {
		return this.compareTo(that) > 0;
	}

	public boolean isEqualTo(Version that) {
		return this.compareTo(that) == 0;
	}

	@Override
	public String toString() {
		return "Version [rawVersion=" + rawVersion + ", versionParts=" + Arrays.toString(versionParts) + "]";
	}

}
