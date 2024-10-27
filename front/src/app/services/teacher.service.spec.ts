import { HttpClientModule } from '@angular/common/http';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { TeacherService } from './teacher.service';
import { Teacher } from '../interfaces/teacher.interface';

describe('TeacherService', () => {
  let service: TeacherService;
  let httpMock: HttpTestingController;

  const mockTeachers: Teacher[] = [
    { id: 1, lastName: 'Doe', firstName: 'John', createdAt: new Date('2023-01-01'), updatedAt: new Date('2023-01-02') },
    { id: 2, lastName: 'Smith', firstName: 'Jane', createdAt: new Date('2023-01-03'), updatedAt: new Date('2023-01-04') }
  ];

  const mockTeacher: Teacher = { id: 1, lastName: 'Doe', firstName: 'John', createdAt: new Date('2023-01-01'), updatedAt: new Date('2023-01-02') };


  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [TeacherService]
    });
    service = TestBed.inject(TeacherService);
    httpMock = TestBed.inject(HttpTestingController);
  });
  afterEach(() => {
    // Vérification des requêtes uniquement après les tests de requêtes HTTP
    if (httpMock.match(() => true).length > 0) {
      httpMock.verify();
    }
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
  it('should retrieve all teachers (GET)', () => {
    service.all().subscribe((teachers) => {
      expect(teachers).toEqual(mockTeachers);
    });
  })
});
