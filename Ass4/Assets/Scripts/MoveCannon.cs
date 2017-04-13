using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class MoveCannon : MonoBehaviour {

	float mouseSpeedX = 6;
	float mouseSpeedY = 2;
	float xRotation = 0;
	float yRotation = 0;

	void Start () {

	}
	
	// Update is called once per frame
	void Update () {
		//Get mouse input
		yRotation = -Mathf.Clamp(Input.GetAxis("Mouse Y") * mouseSpeedY, -40f, 100f);
		xRotation = Input.GetAxis("Mouse X") * mouseSpeedX;

		//Apply to camera and player
		Camera.main.transform.eulerAngles += new Vector3(yRotation, 0f, 0f);
		transform.eulerAngles += new Vector3(0f, xRotation, 0f);
	}
}
