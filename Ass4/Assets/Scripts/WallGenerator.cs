using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class WallGenerator : MonoBehaviour {
	public GameObject[] blocks;
	private bool ifBlue = true;

	void Start () {
		for(int x = -5; x <= 5; x++ ){
			for(int y = 1; y <= 5 ; y++ ){
				if (ifBlue) {
					GameObject BlueCube = GameObject.Instantiate (blocks [0], new Vector3 (x, y, 0), Quaternion.identity) as GameObject;
				} else {
					GameObject RedCube = GameObject.Instantiate (blocks [1], new Vector3 (x, y, 0), Quaternion.identity) as GameObject;
				}
				ifBlue = !ifBlue;
			}
		}	
	}

}
