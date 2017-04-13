using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class Timer : MonoBehaviour {

	public static float timeLeft = 30;
	public Text timeText;
	public Text statusText;
	private string playerStatus = "playing";

	void Start () {
		timeText.text = "00:" + Mathf.Round(timeLeft);
		statusText.text = " ";
	}
	
	// Update is called once per frame
	void Update () {
		if (Mathf.Round (timeLeft) == -1) {
			statusText.text = "YOU LOSE!";	
			playerStatus = "lost";
		} else if (playerStatus == "playing"){
			updateTimer ();
		}
	}

	void updateTimer(){
		timeLeft =  timeLeft - Time.deltaTime;
		timeText.text = "00:" + Mathf.Round(timeLeft);
	}

	void OnCollisionExit (Collision col)
	{
		if((col.gameObject.name == "CannonBallYellow" || col.gameObject.name == "CannonBallYellow(Clone)" || col.gameObject.name == "CannonBall" || col.gameObject.name == "CannonBall(Clone)") && playerStatus == "playing")
		{
			statusText.text = "YOU WIN!";
			playerStatus = "won";
		}
	}


}
