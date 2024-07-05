package com.consensys.merkletree.tree;

import com.consensys.merkletree.util.HashUtil;

import java.security.NoSuchAlgorithmException;
import java.util.*;

public class MerkleTree {
    private MerkleNode root;
    private final Map<String, Integer> leavesMap = new HashMap<>();
    private final List<MerkleNode> leavesList = new ArrayList<>();

    public MerkleTree(List<String> leaves) throws NoSuchAlgorithmException {
        if (leaves.isEmpty()) {
            return;
        }
        for (String leaf : leaves) {
            MerkleNode node = new MerkleNode(HashUtil.hash(List.of(leaf)));
            this.leavesList.add(node);
            this.leavesMap.put(node.getHash(), this.leavesList.size() - 1);
        }
        buildTree();
    }

    public MerkleNode getRoot() {
        return root;
    }

    public void insertLeaf(String leaf) throws NoSuchAlgorithmException {
        String hash = HashUtil.hash(List.of(leaf));
        Integer index = this.leavesMap.get(hash);
        if (index == null) {
            MerkleNode node = new MerkleNode(hash);
            this.leavesList.add(node);
            this.leavesMap.put(node.getHash(), this.leavesList.size() - 1);
            buildTree();
        }
    }

    public void updateLeaf(String oldLeaf, String newLeaf) throws NoSuchAlgorithmException {
        String oldHash = HashUtil.hash(List.of(oldLeaf));
        Integer index = this.leavesMap.get(oldHash);
        if (index != null) {
            String newHash = HashUtil.hash(List.of(newLeaf));
            MerkleNode node = new MerkleNode(newHash);
            this.leavesList.set(index, node);
            this.leavesMap.remove(oldHash);
            this.leavesMap.put(node.getHash(), index);
            buildTree();
        }
    }

    public MerkleProof generateMerkleProof(String leaf) throws NoSuchAlgorithmException {
        String hashLeaf = HashUtil.hash(List.of(leaf));
        Integer index = leavesMap.get(hashLeaf);
        if (index == null) return null;

        List<String> proof = new ArrayList<>();
        List<Boolean> proofIsLeftSibling = new ArrayList<>();
        MerkleNode current = leavesList.get(index);
        while (current != root) {
            MerkleNode parent = findParent(root, current);
            if (parent.getLeft().equals(current)) {
                proof.add(parent.getRight().getHash());
                proofIsLeftSibling.add(false);
            } else {
                proof.add(parent.getLeft().getHash());
                proofIsLeftSibling.add(true);
            }
            current = parent;
        }
        return new MerkleProof(leaf, proof, proofIsLeftSibling);
    }

    public boolean verifyMerkleProof(MerkleProof proof) throws NoSuchAlgorithmException {
        String hash = HashUtil.hash(List.of(proof.getLeaf()));
        for (int i = 0; i < proof.getProof().size(); i++) {
            if (proof.getProofIsLeftSibling().get(i)) {
                hash = HashUtil.hash(List.of(proof.getProof().get(i), hash));
            } else {
                hash = HashUtil.hash(List.of(hash, proof.getProof().get(i)));
            }
        }
        return hash.equals(this.root.getHash());
    }

    private void buildTree() throws NoSuchAlgorithmException {
        List<MerkleNode> nodes = new ArrayList<>(this.leavesList);
        while (nodes.size() > 1) {
            List<MerkleNode> nextLevel = new ArrayList<>();
            for (int i = 0; i < nodes.size(); i += 2) {
                if (i + 1 < nodes.size()) {
                    nextLevel.add(new MerkleNode(nodes.get(i), nodes.get(i + 1)));
                } else {
                    nextLevel.add(nodes.get(i));
                }
            }
            nodes = nextLevel;
        }
        this.root = nodes.get(0);
    }

    private MerkleNode findParent(MerkleNode root, MerkleNode child) {
        if (root == null || (root.getLeft() == null && root.getRight() == null)) return null;
        if (root.getLeft().equals(child) || root.getRight().equals(child)) return root;
        MerkleNode leftResult = findParent(root.getLeft(), child);
        return (leftResult != null) ? leftResult : findParent(root.getRight(), child);
    }
}
