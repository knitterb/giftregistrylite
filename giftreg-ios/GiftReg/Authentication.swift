//
//  Authentication.swift
//  GiftReg
//
//  Created by Angie Lin on 10/26/14.
//  Copyright (c) 2014 Brandon Knitter. All rights reserved.
//

import Foundation

protocol AuthenticationProtocol {
    func didReceiveResponse(auth: Authentication)
}


class Authentication: NSObject {
    
    var data: NSMutableData = NSMutableData()
    var delegate: AuthenticationProtocol?
    
    var username:String = ""
    var password:String = ""
    var token:String = ""
    var tokenIsValid:Bool = false
    
    func authUser(username: String, password: String) {
        self.username=username
        self.password=password
        self.token=""
        self.tokenIsValid=false
    
        var urlString="http://localhost:8888/_ah/api/userendpoint/v1/authenticateUser/\(username)/\(password)"
        var url: NSURL = NSURL(string: urlString)!
        var req: NSMutableURLRequest = NSMutableURLRequest(URL: url)
        req.HTTPMethod="POST"
        var conn: NSURLConnection = NSURLConnection(request: req, delegate: self, startImmediately: false)!
        
        println("authUser(): \(urlString)")
        conn.start()
    }
    
    func validateToken(token: String) {

    }
    
    func connection(connection: NSURLConnection!, didFailWithError response: NSError!) {
        println("Authentication.connection(): error \(response.localizedDescription)")
    }
    
    func connection(connection: NSURLConnection!, didReceiveReponse response: NSURLResponse!) {
        self.data=NSMutableData()
    }
    
    func connection(connection: NSURLConnection!, didReceiveData response: NSData!) {
        self.data.appendData(response)
    }
    
    func connectionDidFinishLoading(connection: NSURLConnection!) {
        println("Authentication finished")
        if self.data.length > 0 {
            var json: NSDictionary = NSJSONSerialization.JSONObjectWithData(self.data, options: NSJSONReadingOptions.MutableContainers, error: nil) as NSDictionary
            let error=json["error"] as? NSDictionary
            if error == nil {
                self.token=json["key"] as String
                self.tokenIsValid=true
            }
        }
        delegate?.didReceiveResponse(self)
    }
    
}
