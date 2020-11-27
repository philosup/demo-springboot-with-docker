package com.philosup.demo;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bouncycastle.jcajce.provider.digest.SHA3;
import org.bouncycastle.util.encoders.Hex;

public class PaymentMerkleTree {
	// Leaf List
	private String[] mainLeafList;
	private String[] subLeafList;
	private String[] additionalLeafList;
	private String[] privateLeafList;

	private Map<String, String> leaves;
	private Boolean treeReadyState;
	private String[][] levels = null;

	public PaymentMerkleTree(String[] _mainLeafList, String[] _subLeafList, String[] _additionalLeafList,
			String[] _privateLeafList) {
		mainLeafList = _mainLeafList;
		subLeafList = _subLeafList;
		additionalLeafList = _additionalLeafList;
		privateLeafList = _privateLeafList;

		leaves = new HashMap<String, String>();
		treeReadyState = false;
		levels = new String[3][4];

		setDefaultLeaves();
	}

	public void resetTree() {
		leaves = new HashMap<String, String>();
		treeReadyState = false;
		levels = new String[3][4];

		setDefaultLeaves();
	}

	private void setDefaultLeaves() {
		// set default leaves value
		for (int i = 0; i < mainLeafList.length; i++) {
			leaves.put(mainLeafList[i], null);
		}

		for (int i = 0; i < subLeafList.length; i++) {
			leaves.put(subLeafList[i], null);
		}

		for (int i = 0; i < additionalLeafList.length; i++) {
			leaves.put(additionalLeafList[i], null);
		}

		for (int i = 0; i < privateLeafList.length; i++) {
			leaves.put(privateLeafList[i], null);
		}

	}

	public void addLeaf(String _key, String _value) throws Exception {
		treeReadyState = false;

		List<String> LeavesKeyList = new ArrayList<>(leaves.keySet());
		if (!(LeavesKeyList.contains(_key))) {
			throw new Exception("Invalid Key");
		}

		leaves.put(_key, _value);

	}

	public void makeTree() throws Exception {
		if (!(getLeavesReadyState())) {
			throw new Exception("Leaves are not Ready");
		}
		setLevel();

		treeReadyState = true;

	}

	private void setLevel() throws UnsupportedEncodingException {
		SHA3.Digest256 digestSHA3 = new SHA3.Digest256();
		String timeLeavesValue = new String();
		String mainLeavesValue = new String();
		String additionalLeavesValue = new String();
		String privateLeavesValue = new String();

		// compute time leaves value; [2,0]
		for (int i = 0; i < mainLeafList.length; i++) {
			timeLeavesValue += leaves.get(mainLeafList[i]).toString();
		}
		byte[] onceHashedTimeLeavesValue = digestSHA3.digest(timeLeavesValue.getBytes("utf-8"));
		byte[] twiceHashedTimeLeavesValue = digestSHA3.digest(onceHashedTimeLeavesValue);
		levels[2][0] = Hex.toHexString(twiceHashedTimeLeavesValue);

		// compute main leaves value; [2,1]
		for (int i = 0; i < subLeafList.length; i++) {
			mainLeavesValue += leaves.get(subLeafList[i]).toString();
		}
		byte[] onceHashedServiceLeavesValue = digestSHA3.digest(mainLeavesValue.getBytes("utf-8"));
		byte[] twiceHashedServiceLeavesValue = digestSHA3.digest(onceHashedServiceLeavesValue);
		levels[2][1] = Hex.toHexString(twiceHashedServiceLeavesValue);

		// compute additional leaves value; [2,2]
		for (int i = 0; i < additionalLeafList.length; i++) {
			additionalLeavesValue += leaves.get(additionalLeafList[i]).toString();
		}
		byte[] onceHashedAdditioanlLeavesValue = digestSHA3.digest(additionalLeavesValue.getBytes("utf-8"));
		byte[] twiceHashedAdditionalLeavesValue = digestSHA3.digest(onceHashedAdditioanlLeavesValue);
		levels[2][2] = Hex.toHexString(twiceHashedAdditionalLeavesValue);

		// compute private leaves value; [2,3]
		for (int i = 0; i < privateLeafList.length; i++) {
			privateLeavesValue += leaves.get(privateLeafList[i]).toString();
		}
		byte[] onceHashedPrivateLeavesValue = digestSHA3.digest(privateLeavesValue.getBytes("utf-8"));
		byte[] twiceHashedPrivateLeavesValue = digestSHA3.digest(onceHashedPrivateLeavesValue);
		levels[2][3] = Hex.toHexString(twiceHashedPrivateLeavesValue);

		// compute left top branch value; [1,0]
		String branchValue_1_0 = levels[2][0] + levels[2][1];
		byte[] onceHashedBranchValue_1_0 = digestSHA3.digest(branchValue_1_0.getBytes("utf-8"));
		byte[] twiceHashedBranchValue_1_0 = digestSHA3.digest(onceHashedBranchValue_1_0);
		levels[1][0] = Hex.toHexString(twiceHashedBranchValue_1_0);

		// compute right top branch value; [1,1]
		String branchValue_1_1 = levels[2][2] + levels[2][3];
		byte[] onceHashedBranchValue_1_1 = digestSHA3.digest(branchValue_1_1.getBytes("utf-8"));
		byte[] twiceHashedBranchValue_1_1 = digestSHA3.digest(onceHashedBranchValue_1_1);
		levels[1][1] = Hex.toHexString(twiceHashedBranchValue_1_1);

		// compute merkle root value; [0,0]
		String merkleRootValue = levels[1][0] + levels[1][1];
		byte[] onceHashedMerkleRootValue = digestSHA3.digest(merkleRootValue.getBytes("utf-8"));
		byte[] twiceHashedMerkleRootValue = digestSHA3.digest(onceHashedMerkleRootValue);
		levels[0][0] = Hex.toHexString(twiceHashedMerkleRootValue);

	}

	public String getLeaf(String _key) throws Exception {
		List<String> LeavesKeyList = new ArrayList<>(leaves.keySet());
		if (!(LeavesKeyList.contains(_key))) {
			throw new Exception("Invalid Key");
		}

		return leaves.get(_key);
	}

	public Set<String> getLeavesKeyList() {
		return leaves.keySet();
	}

	public Boolean getLeavesReadyState() {
		List<String> LeavesKeyList = new ArrayList<>(leaves.keySet());
		for (int i = 0; i < LeavesKeyList.size(); i++) {
			if (leaves.get(LeavesKeyList.get(i)) == null) {
				return false;
			}
		}
		return true;
	}

	public Boolean getTreeReadyState() {
		return treeReadyState;
	}

	public String getMerkleRoot() throws Exception {
		if (treeReadyState) {
			return levels[0][0];
		} else {
			throw new Exception("Tree is not Ready");
		}
	}

	public String[] getProof(String _index) throws Exception {
		if (!(treeReadyState)) {
			throw new Exception("Tree is not Ready");
		} else if (_index.equals("0")) {
			return new String[] { levels[1][0], levels[1][1] };
		} else if (_index.equals("1")) {
			return new String[] { levels[2][0], levels[2][1] };
		} else if (_index.equals("2")) {
			return new String[] { levels[2][2], levels[2][3] };
		} else {
			return null;
		}

	}

}
