import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionService } from './session.service';
import { SessionInformation } from '../interfaces/sessionInformation.interface';

describe('SessionService', () => {
  let service: SessionService;
  const mockUser: SessionInformation = {
    token: "string",
  type: "string",
  id: 1,
  username: "string",
  firstName: "string",
  lastName: "string",
  admin: false
  };

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SessionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should start with isLogged as false', () => {
    expect(service.isLogged).toBe(false);
  });
  it('should update isLogged to true on login', () => {
    service.logIn(mockUser);
    expect(service.isLogged).toBe(true);
    expect(service.sessionInformation).toEqual(mockUser);
  });
  it('should update isLogged to false on logout', () => {
    service.logIn(mockUser);
    service.logOut();
    expect(service.isLogged).toBe(false);
    expect(service.sessionInformation).toBeUndefined();
  });
});
