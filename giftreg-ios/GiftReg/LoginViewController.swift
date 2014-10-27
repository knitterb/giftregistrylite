//
//  LoginViewController.swift
//  GiftReg
//
//  Created by Angie Lin on 10/26/14.
//  Copyright (c) 2014 Brandon Knitter. All rights reserved.
//

import UIKit

class LoginViewController: UIViewController, AuthenticationProtocol {
    
    @IBOutlet weak var textUsername: UITextField!
    @IBOutlet weak var textPassword: UITextField!
    @IBOutlet weak var buttonLogin: UIButton!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        
      }
    
    override func viewDidAppear(animated: Bool) {
        // for once it shows up
        var userDefaults = NSUserDefaults.standardUserDefaults()
        if true || userDefaults.objectForKey("token") == nil {
            // check if we have a token, if not make this show up
            textUsername.hidden=false
            textPassword.hidden=false
            buttonLogin.hidden=false
        }

    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    
    @IBAction func buttonLoginClick(sender: UIButton) {
        
        var auth: Authentication = Authentication()
        
        textUsername.enabled=false
        textPassword.enabled=false
        buttonLogin.enabled=false
        auth.delegate=self
        auth.authUser(textUsername.text, password: textPassword.text)
        
    }
    
    func didReceiveResponse(auth: Authentication) {
        if auth.tokenIsValid {
            println("auth succeeded: "+auth.token)
            var userDefaults = NSUserDefaults.standardUserDefaults()
            userDefaults.setValue(auth.token, forKey: "token")
            userDefaults.setValue(auth.username, forKey: "username")
            
            // TODO: Open the next screen
            let vc = self.storyboard?.instantiateViewControllerWithIdentifier("mainView") as UIViewController
            self.presentViewController(vc , animated: true, completion: nil)
            
        } else {
            println("auth failed")
            let alert = UIAlertView()
            alert.title = "Login Failure"
            alert.message = "Username or Password is invalid."
            alert.addButtonWithTitle("OK")
            alert.show()
            
            textUsername.enabled=true
            textPassword.enabled=true
            buttonLogin.enabled=true

        }
    }
    
    
}

