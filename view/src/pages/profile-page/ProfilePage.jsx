export const URL = '/profile';
export const NAME = "Profile";

export const ProfilePage = ({user}) => {
  return (
    <div>
      <h1>
        Profile Page
      </h1>
      <h2>
        {`Hello, ${user.currentUser.name}`}
      </h2>
    </div>
  )
}