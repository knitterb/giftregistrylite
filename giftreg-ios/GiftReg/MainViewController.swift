//
//  MainViewController.swift
//  GiftReg
//
//  Created by Angie Lin on 10/26/14.
//  Copyright (c) 2014 Brandon Knitter. All rights reserved.
//

import Foundation
import UIKit

class MainViewController: UIViewController {
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        
    }
    
    override func viewDidAppear(animated: Bool) {
        // for once it shows up
        var userDefaults = NSUserDefaults.standardUserDefaults()
        if true || userDefaults.objectForKey("token") == nil {
            // check if we have a token, if not make this show up
        }
        
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
}

