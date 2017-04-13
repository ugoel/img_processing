using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class FireBall : MonoBehaviour {
	public GameObject[] balls;
	private int count = 0;
	private int power = 3000;

	void Start () {
		
	}

	void Update () {
		Fire ();
	}

	public void Fire() {
		if (Input.GetMouseButtonDown (0) || Input.GetMouseButtonDown (1) || Input.GetMouseButtonDown (2)) {
			if (count % 2 == 0) {
				GameObject YellowBall = GameObject.Instantiate (balls [1], transform.position, transform.rotation) as GameObject;
				Rigidbody rbY = YellowBall.GetComponent<Rigidbody> ();
				rbY.AddForce(transform.forward * power + transform.up * power/3);
			Destroy (YellowBall, 5f);
			}
			else {
				GameObject BlackBall = GameObject.Instantiate (balls [0], transform.position, transform.rotation) as GameObject;
				Rigidbody rbB = BlackBall.GetComponent<Rigidbody> ();
				rbB.AddForce(transform.forward * power + transform.up * power/3);
				Destroy (BlackBall, 5f);
			}
			count++;
		}
	}
}
