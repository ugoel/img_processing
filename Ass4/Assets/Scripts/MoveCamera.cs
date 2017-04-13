using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class MoveCamera : MonoBehaviour {

	// Use this for initialization
	void Start () {
		
	}
	
	// Update is called once per frame
	void Update () {
		if (Input.GetKeyDown(KeyCode.LeftArrow) || Input.GetKeyDown(KeyCode.A)){
			transform.position = new Vector3(transform.position.x - 1, transform.position.y, transform.position.z);
		} 
		else if (Input.GetKeyDown(KeyCode.RightArrow) || Input.GetKeyDown(KeyCode.D)){
			transform.position = new Vector3(transform.position.x + 1, transform.position.y, transform.position.z);
		}

		if (Input.GetKeyDown(KeyCode.UpArrow) || Input.GetKeyDown(KeyCode.W)){
			transform.position = new Vector3(transform.position.x, transform.position.y + 1, transform.position.z);
		} 
		else if (Input.GetKeyDown(KeyCode.DownArrow) || Input.GetKeyDown(KeyCode.S)){
			transform.position = new Vector3(transform.position.x, transform.position.y - 1, transform.position.z);
		}
	}
}
