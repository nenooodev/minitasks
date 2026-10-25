# Aprendizaje TDD — Mini Tasks

## SimpleTask

- **Test que produjo el rojo:** `SimpleTaskTest.constructor_storesIdTitleAndDone`
  y `SimpleTaskTest.setters_updateValues`.
- **Por qué falló:** el test no compilaba:
  `cannot find symbol: class SimpleTask`.
- **Cambio que lo hizo pasar:** se creó `SimpleTask` con `id`, `title`,
  `done`, constructor vacío, constructor completo y getters/setters.
  Los 2 tests pasaron.

## CreateTaskDTO

- **Test que produjo el rojo:** `CreateTaskDTOTest.storesTitle`,
  `CreateTaskDTOTest.blankTitle_isInvalid` y
  `CreateTaskDTOTest.nonBlankTitle_isValid`.
- **Por qué falló:** el test no compilaba:
  `cannot find symbol: class CreateTaskDTO`.
- **Cambio que lo hizo pasar:** se creó el record
  `CreateTaskDTO(@NotBlank String title)`. Los 3 tests pasaron, incluida
  la validación con Jakarta Validation.

## ApiExceptionHandler

- **Test que produjo el rojo:** `ApiExceptionHandlerTest.illegalArgument_returns404WithMessage`
  y `ApiExceptionHandlerTest.validationError_returns400WithFieldDetails`.
- **Por qué falló:** el test no compilaba:
  `cannot find symbol: class ApiExceptionHandler`.
- **Cambio que lo hizo pasar:** se implementó el `@RestControllerAdvice`
  con `IllegalArgumentException -> 404` y
  `MethodArgumentNotValidException -> 400` con el detalle de los campos.
- **Incidencia en el primer verde:** el test de validación falló con
  `NotReadablePropertyException: Invalid property 'title' of bean class
  [java.lang.Object]` porque se usaba `rejectValue` sobre un `Object` sin
  propiedad `title`. Se corrigió el test añadiendo un `FieldError`
  directamente; después los 2 tests pasaron.

## TaskControllerV1

- **Test que produjo el rojo:** `TaskControllerV1Test.ping_returnsPong`,
  `TaskControllerV1Test.create_returns201_and_body` y
  `TaskControllerV1Test.list_returnsCreatedTask`.
- **Por qué falló:** el test no compilaba:
  `cannot find symbol: class TaskControllerV1`. Además, el test existente
  estaba en `src/test/` (fuera de `src/test/java/`), así que Maven no lo
  compilaba; se movió a su paquete.
- **Cambio que lo hizo pasar:** se implementó `/v1/tasks` en memoria:
  ping, sample, listado, creación con 201 y borrado con 204. El campo de
  estado es `done`, no `completed`.
- **Aislamiento:** `list_returnsCreatedTask` crea su propia tarea con POST
  y comprueba que aparece con `hasItem`; no depende de lo que haya hecho
  otro método de prueba.

## TaskControllerV2

- **Test que produjo el rojo:** `TaskControllerV2Test.sample_returnsMessageAndTime`,
  `TaskControllerV2Test.list_returnsCreatedTask` y
  `TaskControllerV2Test.create_returns201_withLocation`.
- **Por qué falló:** el test no compilaba:
  `cannot find symbol: class TaskControllerV2`.
- **Cambio que lo hizo pasar:** se implementó `/v2/tasks` con
  `ResponseEntity`, incluyendo el 201 con cabecera
  `Location: /v2/tasks/{id}`. Los 3 tests pasaron.

## Feature: GET /v1/tasks/{id} (tarea existente)

- **Test que produjo el rojo:** `TaskControllerV1Test.getById_returnsCreatedTask`.
  El test crea la tarea con POST y la consulta con GET dentro del mismo
  test.
- **Por qué falló:** `Status expected:<200> but was:<405>`. La ruta
  `/v1/tasks/{id}` solo tenía `DELETE`, así que Spring rechazaba el GET
  con 405.
- **Cambio que lo hizo pasar:** se añadió
  `@GetMapping("/tasks/{id}")`, que busca en la lista en memoria y
  devuelve la tarea encontrada (con `orElse(null)` para el caso que aún
  no existía).

## Feature: GET /v1/tasks/{id} (id inexistente)

- **Test que produjo el rojo:** `TaskControllerV1Test.getById_nonExisting_returns404`.
- **Por qué falló:** `Status expected:<404> but was:<200>`. Al devolver
  `null`, Spring respondía 200 con cuerpo vacío.
- **Cambio que lo hizo pasar:** se cambió `orElse(null)` por
  `orElseThrow(() -> new IllegalArgumentException("Task not found"))`.
  `ApiExceptionHandler` ya traducía esa excepción a 404 con
  `{"error":"Task not found"}`.

Tests MVC de V1 (listado y creación) y V2 (201 con `Location`) incluidos
en la suite, además de los dos casos nuevos de `GET /v1/tasks/{id}`.

## TaskService: normalización del título (sesión 3)

### Rojo

- **Test que produjo el rojo:** `TaskServiceTest.createStripsTitleEdges`.
- **Commit:** `8d176e6` (`[RED]: TaskService.createStripsTitleEdges`),
  tag `sesion-3-8d176e6`.
- **Por qué falló:** el servicio inicial guardaba `dto.title()` sin
  normalizar:

  ```text
  expected: "Repasar Spring Boot"
   but was: "  Repasar Spring Boot  "
  ```

- **Comando:** `bash mvnw -Dtest=TaskServiceTest#createStripsTitleEdges test`.
- **Repositorio mock:** `repository.save(...)` devuelve su argumento y el
  test comprueba el título de la tarea devuelta.

### Verde

- **Commit:** `f5c7972` (`[GREEN]: TaskService.createStripsTitleEdges con strip()`),
  tag `sesion-3-f5c7972`.
- **Cambio que lo hizo pasar:** `dto.title().strip()` antes de construir
  `SimpleTask`, manteniendo `done = false`.
- **Resultado:** el test objetivo y la suite completa pasaron.

### Refactor

- **Commit:** `fd38260`
  (`[REFACTOR]: TaskService conserva espacios interiores y done=false`),
  tag `sesion-3-fd38260`. Añade el caso con `"Repasar Spring Boot"` y
  comprueba que se conserva el título y que `done` es `false`.
- **Commit:** `7bedc18`
  (`[REFACTOR]: TaskControllerV1 delega en TaskService`),
  tag `sesion-3-7bedc18`. El controlador ya no guarda estado: delega
  listado, consulta, creación y borrado en el servicio. Las pruebas MVC
  existentes se conservan con `@Import` del servicio y del repositorio.
- **Commit:** `52f165f`
  (`[REFACTOR]: Excepción propia TaskNotFoundException`),
  tag `sesion-3-52f165f`. El servicio lanza la excepción propia y
  `ApiExceptionHandler` la traduce a 404 con
  `{"error":"Task not found"}`.
- **Repositorio:** `TaskRepository` e `InMemoryTaskRepository` mantienen
  el almacenamiento en memoria y la asignación de ids.


## Declaración de IA
He usado la IA para la redacción de este aprendizaje.md, asi como para añadir algunos tests extra que no aparecian en el repo original, por supuesto consultando aquellos métodos que me resultaban nuevos 

