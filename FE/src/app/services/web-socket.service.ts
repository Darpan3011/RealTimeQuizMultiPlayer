import { Injectable } from '@angular/core';
import { Client, Message } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class WebSocketService {
  private client: Client;
  private connectionStatus = new BehaviorSubject<boolean>(false);

  constructor() {
    this.client = new Client({
      webSocketFactory: () => new SockJS('http://localhost:8081/ws'),
      debug: (msg: string) => console.log(msg),
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000,
    });

    this.client.onConnect = (frame) => {
      console.log('Connected: ' + frame);
      this.connectionStatus.next(true);
    };

    this.client.onStompError = (frame) => {
      console.error('Broker reported error: ' + frame.headers['message']);
      console.error('Additional details: ' + frame.body);
    };

    this.client.activate();
  }

  public getConnectionStatus(): Observable<boolean> {
    return this.connectionStatus.asObservable();
  }

  public subscribe(topic: string, callback: (message: any) => void) {
    // Wait for connection
    const sub = this.connectionStatus.subscribe(connected => {
      if (connected) {
        this.client.subscribe(topic, (message: Message) => {
          callback(JSON.parse(message.body));
        });
        sub.unsubscribe(); // Only subscribe once to the connection event
      }
    });
  }

  public send(destination: string, body: any) {
    if (this.client.connected) {
      this.client.publish({ destination, body: JSON.stringify(body) });
    }
  }
}
