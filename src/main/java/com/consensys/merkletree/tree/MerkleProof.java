package com.consensys.merkletree.tree;

import java.util.List;

public class MerkleProof {
    private final String leaf;
    private final List<String> proof;
    private final List<Boolean> proofIsLeftSibling;

    public MerkleProof(String leaf, List<String> proof, List<Boolean> proofIsLeftSibling) {
        this.leaf = leaf;
        this.proof = proof;
        this.proofIsLeftSibling = proofIsLeftSibling;
    }

    public String getLeaf() {
        return leaf;
    }

    public List<String> getProof() {
        return proof;
    }

    public List<Boolean> getProofIsLeftSibling() {
        return proofIsLeftSibling;
    }
}
