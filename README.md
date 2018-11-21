# poker-ynov-game-api


### /users/join

Add a user to a game and returns the game after adding the user.

---
##### ***POST***

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| id | body | User Id | Yes | number |
| email | body | User Email | Yes | string |
| username | body | User Username | Yes | string |
| password | body | User Password | Yes | string |
| money | body | User Money | Yes | number |

**Responses**

| Code | Description |
| ---- | ----------- |
| 200 | return game |
| 500 | Internal server error |



### /action

Proceed an action and returns game after action process.

---
##### ***POST***

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| gameId | body | Game Id | Yes | number |
| userId | body | User Id | Yes | string |
| actionType | body | Action played (CALL|FOLD|BET) | Yes | string |
| value | body | money bet only if actionType = "BET"  | No | number |

**Responses**

| Code | Description |
| ---- | ----------- |
| 200 | return game |
| 500 | Internal server error |


### /game/{gameId}/users/{userId}/cards

---
##### ***GET***

Returns the downcards of the user for a game.

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| gameId | path | Game Id | Yes | number |
| userId | path | User Id | Yes | string |

**Responses**

| Code | Description |
| ---- | ----------- |
| 200 | return game |
| 500 | Internal server error |
